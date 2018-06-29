const functions = require('firebase-functions');
var Nexmo = require('nexmo');
const adminAcl = {
  "paths": {
    "/v1/users/**": {},

    "/v1/conversations/**": {},

    "/v1/sessions/**": {},

    "/v1/devices/**": {},

    "/v1/image/**": {},

    "/v3/media/**": {},

    "/v1/applications/**": {},
    
    "/v1/push/**": {},
    
    "/v1/knocking/**": {}
  }
}

exports.answer = functions.https.onRequest((request, response) => {
  //use the `to` query parameter that Nexmo gives us to make a call.
  //if `to` is null, then we are receiving a call.
  var to = request.query.to
  var from = request.query.from

  var ncco = [];

  if (to) {
    ncco.push(
      {
        action: "talk",
        text: "Thank you for calling, you are now being connected."
      },
      {
        "action": "connect",
        "from": functions.config().nexmo.from_number,
        "endpoint": [
          {
            "type": "phone",
            "number": `${to}`
          }
        ]
      }
    )
  } else {
    ncco.push(
      {
        action: "talk",
        text: "You are being connected to the Guest tablet."
      },
      {
        "action": "connect",
        "from": from,
        "endpoint": [
          {
            "type": "app",
            "user": "Guest tablet"
          }
        ]
  
      })
  }
  response.json(ncco);
});

exports.event = functions.https.onRequest((request, response) => {
  console.log(request.body);
  response.sendStatus(200)
});

exports.jwt = functions.https.onRequest((request, response) => {
    response.json({
      user_jwt: Nexmo.generateJwt("private.key", {
        application_id: functions.config().nexmo.application_id,
        sub: "Guest tablet",
        exp: new Date().getTime() + 86400,
        acl: adminAcl
      })
    });
});
  
