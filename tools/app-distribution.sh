export GOOGLE_APPLICATION_CREDENTIALS=google-credentials.json

RELEASE_NOTES_FILE_NAME="release_notes.txt"
DEFAULT_QR_LINK="https://chatgpt.com/" #Todo update
BUILD_FILE="app/build.gradle.kts"

WEBHOOK_URL="" # Todo update
IMGBB_API_KEY="f2e8fd2d643ee693527a3a24c7ea9b42"

branch=$(git branch --show-current)
commit_message=$(git log -1 --format=%s)
commit_hash=$(git rev-parse --short HEAD)
ticket=$(echo "$commit_message" | grep -oE '[A-Z]+-[0-9]+' | head -n 1)
# If no ticket is found, set default values
if [ -z "$ticket" ]; then
  ticket="JIRA"
  ticket_url="https://jira.atlassian.net" # Todo update
else
  ticket_url="https://jira.atlassian.net/browse/$ticket"
fi

## Create Version Code
start_time=$(date -j -f "%d/%m/%Y %H:%M:%S" "21/06/2021 00:00:00" "+%s")
current_time=$(date "+%s")
diff_seconds=$((current_time - start_time))
newVersionCode=$((diff_seconds / 60))

## Current Version Name and Code
versionCode=$(sed -n 's/.*versionCode *= *\([0-9]*\).*/\1/p' "$BUILD_FILE")
versionName=$(sed -n 's/.*versionName *= *"\(.*\)".*/\1/p' "$BUILD_FILE")
echo "versionCode: $newVersionCode"
echo "versionName: $versionName"

## Replace version code
sed -i '' -E "s/(versionCode *= *)[0-9]+/\1$newVersionCode/" "$BUILD_FILE"

# Read release notes from file (make sure file exists)
# File to store the commit history
touch "$RELEASE_NOTES_FILE_NAME"
echo "$commit_message" > "$RELEASE_NOTES_FILE_NAME"
echo "Generate release notes: $commit_message"

if [ -f "$RELEASE_NOTES_FILE_NAME" ]; then
  release_notes=$(<"$RELEASE_NOTES_FILE_NAME")
else
  release_notes="(No release notes found)"
fi

echo "Build and Publish to Firebase..."
gradle_output=$(./gradlew clean assembleStagingDebug appDistributionUploadStagingDebug)
echo "$gradle_output"
FIREBASE_URL=$(echo "$gradle_output" | grep -oE 'https://firebaseappdistribution\.googleapis\.com[^ ]+app\.apk[^ ]*' | head -n 1)

if [ -z "$FIREBASE_URL" ]; then
  echo "Error: Failed to upload the app to Firebase App Distribution. Please try again!"
  exit 1
fi

# Generate QR code image from the Firebase URL
# Check if qrencode is installed, if not install via Homebrew
if ! command -v qrencode &> /dev/null; then
  echo "qrencode is not installed. Installing with Homebrew..."
  if ! command -v brew &> /dev/null; then
    echo "Homebrew is not installed. Please install Homebrew first: https://brew.sh/"
  else
    brew install qrencode
  fi
fi

echo "Generate QR Code"
QR_IMAGE="firebase_qr_$newVersionCode.png"
# Attempt to generate QR code
if qrencode -o "$QR_IMAGE" -s 2 "$FIREBASE_URL"; then
  echo "QR Code generated successfully at $QR_IMAGE"

  echo "Uploading QR code image to ImgBB..."
  IMGBB_RESPONSE=$(curl -s -X POST "https://api.imgbb.com/1/upload" \
    -F "key=$IMGBB_API_KEY" \
    -F "image=@${QR_IMAGE}")

  # Extract the display_url (direct image link)
  QR_IMAGE_URL=$(echo "$IMGBB_RESPONSE" | jq -r '.data.display_url')
  if [ "$QR_IMAGE_URL" == "null" ] || [ -z "$QR_IMAGE_URL" ]; then
    echo "Error uploading image to ImgBB. Response: $IMGBB_RESPONSE"
    QR_IMAGE_URL="$DEFAULT_QR_LINK"  # fallback: no image link
  else
    echo "QR code image uploaded: $QR_IMAGE_URL"
  fi
else
  # This block runs if qrencode fails
  echo "Error: Failed to generate QR code for $FIREBASE_URL."
  QR_IMAGE_URL="$DEFAULT_QR_LINK" # Ensure QR_IMAGE_URL is firebase link if QR generation fails
fi

# Remove files
rm $RELEASE_NOTES_FILE_NAME
rm $QR_IMAGE
# Revert version code
sed -i '' -E "s/(versionCode *= *)[0-9]+/\1$versionCode/" "$BUILD_FILE"

## Send Slack message
echo "Send Slack Message"
release_notes_with_links=$(echo "$release_notes" | sed -E 's/\[([A-Z]+-[0-9]+)\]/[<https:\/\/castalk.atlassian.net\/browse\/\1|\1>]/g')
echo $release_notes_with_links

payload=$(jq -n \
  --arg header ":tada: Android App is Ready!" \
  --arg version "*Version:* $versionName ($newVersionCode)" \
  --arg branch "*Branch:* $branch" \
  --arg commit_message "*Release Notes:* $commit_message" \
  --arg hash "*Commit Hash:* $commit_hash" \
  --arg button_text "Install APK" \
  --arg button_url "$FIREBASE_URL" \
  --arg ticket_text "$ticket" \
  --arg ticket_url "$ticket_url" \
  --arg qr_image_url "$QR_IMAGE_URL" \
  '{
    blocks: [
      {
        type: "header",
        text: {
          type: "plain_text",
          text: $header,
          emoji: true
        }
      },
      {
        type: "section",
        text: {
          type: "mrkdwn",
          text: $commit_message
        }
      },
      {
        type: "section",
        fields: [
          { type: "mrkdwn", text: $version },
          { type: "mrkdwn", text: $branch },
          { type: "mrkdwn", text: $hash }
        ]
      },
      {
        type: "image",
        image_url: $qr_image_url,
        alt_text: "Scan to install"
      },
      {
        type: "actions",
        elements: [
          {
            type: "button",
            text: {
              type: "plain_text",
              text: $button_text,
              emoji: true
            },
            url: $button_url,
            style: "primary"
          },
          {
            type: "button",
            text: {
              type: "plain_text",
              text: $ticket_text,
              emoji: true
            },
            url: $ticket_url,
            style: "danger"
          }
        ]
      }
    ]
  }')

echo "$payload"

# Send the notification using curl
curl -X POST -H 'Content-type: application/json' --data "$payload" "$WEBHOOK_URL"
