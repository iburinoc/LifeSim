var http = require('http');
var url = require('url');
var fs = require('fs');

var score;

function writeScores(response) {
	response.writeHead(200, {'Content-Type': 'text/plain'});
	var i = 0;
	while(score['name'+i] !== undefined) {
		response.write(score['name'+i] + ',' + score['score'+i]+';');
		i++;
	}
	response.end();
}

function onRequest(request, response) {
	console.log(request.url);
	if(request.method == 'GET') {
		writeScores(response); 
	}
}

function start() {
	http.createServer(onRequest).listen(3005);
	fs.readFile('./score.txt', 'utf8', function(err, data) {
		if(err) {
			score = {};
		} else {
			score = JSON.parse(data);
		}
		console.log(score);
	});
	console.log("High Score server started");
}

start();
