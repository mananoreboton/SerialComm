# SerialComm
Serial communication Arduino-Java framework

        int command = 2;
        SerialConversation conversation = new SerialConversation(true);
        SerialActions actions = new SerialActions();
        actions.put(command, x -> {
            return new String(x);
        });
        SerialListener listener = new SerialListener(actions);
        conversation.start(listener);
        conversation.talkAndOver(new SerialMessage(command, "message".getBytes()));

