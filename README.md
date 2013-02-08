# Arastreju #

## Java based engine for semantic graphs ##

Copyright 2008-2013 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH

Licensed under Apache License version 2.0

see http://www.apache.org/licenses/LICENSE-2.0.txt

## Quickstart ##

The entry to the Semantic Graph Engine is the _ArastrejuGate_.

    ArastrejuGate gate = Arasteju.getInstance().openGate("myDomain");

All operation on the semantic graph are done in _Conversations_. To start a new conversation simply type:

    Conversation conversation = gate.startConversation();`

Now we can start to create some nodes and add associations between them.

    // create two nodes
    ResourceNode bart = new SNResoure();
    ResourceNode milhouse = new SNResoure();

    // make both nodes of type 'person'
    bart.addAssociation(RDF.type, MyConstants.PERSON);    
    milhouse.addAssociation(RDF.type, MyConstants.PERSON);
    
    // now add names
    bart.addAssociation(ARAS.HAS_FORENAME, new SNText("Bart"));    
    milhouse.addAssociation(ARAS.HAS_FORENAME, new SNText("Milhouse"));
    
    // and connect the two (put back inferencing now)
    bart.addAssociation(MyConstants.HAS_FRIEND, milhouse);    
    milhouse.addAssociation(MyConstants.HAS_FRIEND, bart);

To persist our little graph we use the conversation.

    conversation.attach(bart);

The node 'bart' is now attached which means two things:
1. It is persistent 
2. All operations to an attached node are reflected directly to the datastore

While attaching a node all associated nodes not yet attached will be attached two.

If we now lookup an existing node for the city 'Springfield' 

    ResourceID springfield = conversation.find(MyConstants.SPRINGFIELD);
    
and add it to 'bart'

    bart.addAssociation(MyConstants.LIVES_IN, springfield).
    
this association will be persisted immediately as 'bart' is still attached.

## Attached and detached nodes ##

If we want to detach a node from the conversation and the underlying datastore we just call

    conversation.detach(bart);
    
Now we can operate on 'bart' without storing this changes.

    ...
    conversation.addAssociation(MyConstants.HAS_SISTER, lisa);
    conversation.addAssociation(MyConstants.HAS_SISTER, maggie);
    ...

As soon if we re-attach 'bart' his relationships will be persistent in the datastore.

    conversation.attach(bart);
    
Detaching has not to be confused with removing.

    conversation.remove(milhouse);
    
Now 'milhouse' is gone and with him all relationships. So if we ask

    bart.hasAssociation(MyConstants.HAS_FRIEND, milhouse);
    
we will get 'false'.

### to be continued... ####





