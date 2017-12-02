const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.sendNotification = functions.database.ref('/eventGuestList/{eventId}/{userId}')
    .onCreate(event => {
        const userId = event.params.userId;
        const eventId = event.params.eventId
        console.log('Push notification triggered');
        console.log('User invited id is', userId);
        // Create a notification
        const payload = {
            notification: {
                title: "Invite notification",
                body: "YOU HAVE BEEN INVITED",
                sound: "default"
            },
        };

        //Create an options object that contains the time to live for the notification and the priority
        const options = {
            priority: "high",
            timeToLive: 60 * 60 * 24
        };
        return admin.messaging().sendToTopic("notification_"+userId, payload, options);
    })
