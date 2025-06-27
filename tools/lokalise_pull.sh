#!/bin/sh

set -e

# Replace with your actual token and project ID
LOKALISE_TOKEN="" # Todo Token
PROJECT_ID="" # Todo Project ID

# Pull translations from Lokalise
unzip_folder="lokalise"
resource_folder="app/src/main/res"

if ! command -v lokalise2 &> /dev/null; then
  echo "lokalise2 CLI not found, installing with Homebrew..."
  if command -v brew &> /dev/null; then
    brew tap lokalise/cli-2
    brew install lokalise2 || { echo "Failed to install lokalise2 CLI"; exit 1; }
  else
    echo "Homebrew not found. Please install Homebrew first: https://brew.sh/"
    exit 1
  fi
fi

lokalise2 \
  --token "$LOKALISE_TOKEN" \
  --project-id "$PROJECT_ID" \
  file download \
  --format xml \
  --original-filenames=true \
  --bundle-structure "values-%LANG_ISO%/strings.xml" \
  --unzip-to "$resource_folder" \
  --export-sort first_added \
  --indentation "4sp" \
