package fr.paris.lutece.plugins.identitystore.modules.taskstack.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

public interface IClientTaskRightDAO
{
    void insert(final ClientTask client, final Plugin plugin) throws JsonProcessingException;

    void delete(final ClientTask client, final Plugin plugin) throws JsonProcessingException;

    List<ClientTask> selectWithAllCodes(final String codeClientSource, final String codeClientTaskUser, final Plugin plugin) throws JsonProcessingException;

    List<ClientTask> selectWithCodeSource(final String codeClientSource, final Plugin plugin) throws JsonProcessingException;

}
