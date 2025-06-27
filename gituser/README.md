# GitUser

## Screenshots

| User List                                             | User Detail                                             |
|-------------------------------------------------------|---------------------------------------------------------|
| <img src="screenshots/git_user_list.png" width=300 /> | <img src="screenshots/git_user_detail.png" width=300 /> |


## Testing

- Run unit testing with coverage:

```
$ ./gradlew koverHtmlReport
```

Report is located at: `app/build/reports/kover/`

<img src="screenshots/koverHtmlReport.png"/>

## Build and deploy

For `release` builds, we need to provide release keystore and signing properties:

- Put the `release.keystore` file at root `config` folder.
- Put keystore signing properties in `signing.properties`
