package fr.paris.lutece.plugins.identitystore.modules.taskstack.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.plugins.identitystore.service.IdentityStorePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

public class ClientTaskRightHome
{
    private static final ClientTaskRightDAO _clientTaskDao = SpringContextService.getBean( ClientTaskRightDAO.BEAN_NAME );
    private static final Plugin _plugin = PluginService.getPlugin(IdentityStorePlugin.PLUGIN_NAME );

    public static void insert( ClientTask clientTask ) throws JsonProcessingException
    {
        _clientTaskDao.insert( clientTask, _plugin );
    }

    public static void delete( ClientTask clientTask ) throws JsonProcessingException
    {
        _clientTaskDao.delete( clientTask, _plugin );
    }

    public static List<ClientTask> getListClientTaskWithAllCodes(String clientCodeSource, String clientCodeTaskUser) throws JsonProcessingException
    {
        return _clientTaskDao.selectWithAllCodes( clientCodeSource, clientCodeTaskUser, _plugin );
    }

    public static List<ClientTask> getListClientTaskWithCodeSource( String clientCodeSource )
    {
        return _clientTaskDao.selectWithCodeSource( clientCodeSource, _plugin );
    }

}
