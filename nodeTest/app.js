var io = require('socket.io').listen(3000);
io.sockets.on('connection', function (socket) {
  console.log("New connection!");
  socket.on('test', function (data,ack) {
    console.log(data);
    ack("hola");
    console.log("I acked");
  });
});

