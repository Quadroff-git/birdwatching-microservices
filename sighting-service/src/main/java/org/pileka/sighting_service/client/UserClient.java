package org.pileka.sighting_service.client;

public interface UserClient {

    boolean userExists(long id);

    String getUsernameById(long id);

}
