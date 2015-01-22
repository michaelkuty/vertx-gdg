
vertx.eventBus().registerHandler("mymod.file.save", new Handler<Message<JsonObject>>() {
  
  	public void handle(Message<JsonObject> msg) {

		eb.send("vertx.auth.authorise", msg.body().getObject("sessionID"), new Handler<Message<JsonObject>>() {
     	    public void handle(Message<JsonObject> reply) {

      			if (reply.body().getObject("status").equals("ok") && reply.body().getObject("username") != null) {
      				//query = ...reply.body().getObject("username")
					eb.send("vertx.mongo.findone", query, new Handler<Message<JsonObject>>() {
			     	    public void handle(Message<JsonObject> result) {

			      			if (reply.body().getObject("status").equals("ok") && reply.body().getObject("result") != null) {
			     	    		
								vertx.fileSystem().open("some-file.dat", new AsyncResultHandler<AsyncFile>() {
								    public void handle(AsyncResult<AsyncFile> ar) {
								        if (ar.succeeded()) {
								            AsyncFile asyncFile = ar.result();
								            // File open, write a buffer 5 times into a file
								            Buffer buff = new Buffer("foo");
								            for (int i = 0; i < 5; i++) {
								                asyncFile.write(buff, buff.length() * i, new AsyncResultHandler<Void>() {
								                    public void handle(AsyncResult ar) {
								                        if (ar.succeeded()) {
								                            log.info("Written ok!");
								                            // etc
								                        } else {
								                            log.error("Failed to write", ar.cause());
								                        }
								                    }
								                });
								            }
								        } else {
								            log.error("Failed to open file", ar.cause());
								        }
								    }
								});
			     	    	} else {
			     	    		// not found
			     	    	}
			     	    }
			     	});
     	    	
     	    	} else {
     	    		// reponse denied
     	    	}
     	    }
     	});
	}
});