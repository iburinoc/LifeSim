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

function addScore(data) {
	var i = 0;
	console.log(data.name + ";" + data.score);
	while(i < 10) {
		if(score['score'+i] == undefined || data.score > score['score'+i]) {
			var j = 8;
			console.log('score added at ' + i);
			while(j >= i) {
				if(score['name'+j] !== undefined) {
					score['name'+(j+1)] = score['name'+j];
					score['score'+(j+1)] = score['score'+j];
				}
				j--;
			}
			score['name'+i] = data.name;
			score['score'+i]= data.score;
			i = 10;
		}
		i++;
	}
	console.log(score);
	fs.writeFile('./score.txt', JSON.stringify(score), function(){});
}

function onRequest(request, response) {
	console.log(request.url);
	if(request.method == 'GET') {
		writeScores(response);
	}else if(request.method == 'POST') {
		request.on('data', function(data) {
			console.log("Post Request: " + data);
			try{
				addScore(JSON.parse(data));
			}
			catch(err) {
				console.log("Invalid post request");
			}
			writeScores(response);
		});
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
