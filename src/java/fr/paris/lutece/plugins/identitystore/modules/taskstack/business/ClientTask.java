package fr.paris.lutece.plugins.identitystore.modules.taskstack.business;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskStatusType;

public class ClientTask
{
    private String clientCodeSource;
    private String clientCodeTaskUser;
    private String askedRights;

    public String getClientCodeSource()
    {
        return clientCodeSource;
    }

    public void setClientCodeSource(String clientCodeSource)
    {
        this.clientCodeSource = clientCodeSource;
    }

    public String getClientCodeTaskUser()
    {
        return clientCodeTaskUser;
    }

    public void setClientCodeTaskUser(String clientCodeTaskUser)
    {
        this.clientCodeTaskUser = clientCodeTaskUser;
    }

    public String getAskedRights()
    {
        return askedRights;
    }

    public void setAskedRights(String askedRights)
    {
        this.askedRights = askedRights;
    }
}
