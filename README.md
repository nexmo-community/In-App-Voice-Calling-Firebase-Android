# Make and receive phone calls in Android apps with Nexmo In-App Voice and Firebase

With [Nexmo In-App Voice](https://developer.nexmo.com/stitch/in-app-voice/overview), you can easily make and receive phone calls with the Android SDK and WebRTC technology. In this tutorial we'll show you how to make a simple Android app that can make outbound phone calls and receive incoming phone calls.

To get the app up and running, we'll use [Firebase Functions](https://firebase.google.com/docs/functions/) to host the [NCCO](https://developer.nexmo.com/voice/voice-api/guides/ncco) and return a [JWT](http://jwt.io/) for users to login with.

This repo contains the [Firebase functions directory](/functions) and the [Android Sample Code.](/in-app-voice-android-demo)

## Before we get started

There are a few things you’ll need before we get started.

1. [Nexmo Account](https://dashboard.nexmo.com/sign-up)
1. [Firebase Account](https://firebase.google.com/)

### Setting up the Firebase Function.

- Clone this repo and navigate to the `functions` directory.

```sh
git clone git@github.com:Nexmo-Community/Stitch-Calliing-Firebase-Android.git
cd Stitch-Calling-Firebase-Android/functions
```

- Follow the for [setup instructions provided by Firebase](https://firebase.google.com/docs/functions/get-started) and create a new project. 

```sh
npm install -g firebase-tools
firebase login
firebase use --add
```

For this project I chose to use JavaScript instead of TypeScript. 

- Add install Nexmo package

```sh
npm install nexmo
```

- Deploy the function

```sh
firebase deploy --only functions
```

- Create a Nexmo Application and set it as a config variable in Firebase

```sh
#Create a Nexmo Application
nexmo app:create "Firebase Functions Nexmo In-App Calling" https://your-project-name.cloudfunctions.net/answer https://your-project-name.cloudfunctions.net/event --keyfile=functions/private.key --type=rtc
#Set the application_id in the Firebase function
firebase functions:config:set nexmo.application_id="aaaaaaaa-bbbb-cccc-dddd-0123456789ab"
```

- Rent a number and link the number to the Nexmo application and Firebase function
```sh
#Search for a number and buy it
nexmo number:buy --country_code US
#Link the number to our Nexmo application
nexmo link:app 12013753230 aaaaaaaa-bbbb-cccc-dddd-0123456789ab
#Set the `from_number` in the Firebase Config variables.
firebase functions:config:set nexmo.from_number="12013753230"
```

### Set up the Android project

- Open the Android Project in the `in-app-voice-android-demo` folder in Android Studio.
- Change the URL in the [`RetrofitClient.kt` file](/in-app-voice-android-demo/app/src/main/java/com/nexmo/inappvoicewithfirebase/networking/RetrofitClient.kt) to the URL Firebase provided you.
- Build and run the app
