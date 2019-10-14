const http = require('http');

let state = {
    plateDelivered: false,
    legsDelivered: false,
    legsAttached: false,
    legsDeliveredX: false,
    legsAttachedX: false,
    redPadAttached: false,
    greenPadAttached: false
};

http.createServer((req, res) => {
    if (req.method === 'POST') {

        let body = '';
        req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
        });
        req.on('end', () => {
            setState(body, true);
            res.end('ok');
        });

    }
    else {
        res.writeHead(200, {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        });
        res.end(JSON.stringify(state));
    }
}).listen(3000);


function setState(stateName, value) {
    switch (stateName) {
        case 'deliver plate':
            state.plateDelivered = true;
            break;

        case 'deliver legs':
            state.legsDelivered = true;
            break;

        case 'attach legs':
            state.legsAttached = true;
            break;
        
        case 'deliver legs X':
            state.legsDeliveredX = true;
            break;

        case 'attach legs X':
            state.legsAttachedX = true;
            break;
        
        case 'attach red pad':
            state.redPadAttached = true;
            break;

        case 'attach green pad':
            state.greenPadAttached = true;
            break;

        case 'reset':
            state.plateDelivered = false;
            state.legsDelivered = false;
            state.legsAttached = false;
            state.legsDeliveredX = false;
            state.legsAttachedX = false;
            state.redPadAttached = false;
            state.greenPadAttached = false;
            break;
    
        default:
            break;
    }

    console.log('=== state is now: ===');
    console.log(stateName);
    console.log('');
       
}
