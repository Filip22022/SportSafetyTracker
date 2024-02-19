# SportSafetyTracker
> Detect accidents and call help

## General Information
- A simple android app using the accelerometer to check if an accident happened to the user, and sending a custom message to a specified number.
- Created for Android Applications class at Silesian Univeristy of Technology


## Technologies Used
- Kotlin
- jetpack Compose


## Features
- Tracking the accelerometer
- Sending sms message to a validated phone number
- Sending location information with the message


## Usage
After starting the tracker, if an unnatural movement is detected, a timer starts and the phone emits a warning signal. If during the coutdown the alarm is not canceled, a sms message will be sent.
The alarm status is cancelled by clicking 'I'm fine' button or if the phone starts moving naturally.

The countdown time, custom message, and phone number can be set in the settings screen.

## Project Status
Project is: _complete_

