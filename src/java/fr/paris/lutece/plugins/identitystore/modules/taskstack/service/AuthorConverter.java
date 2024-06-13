package fr.paris.lutece.plugins.identitystore.modules.taskstack.service;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.taskstack.dto.AuthorDto;

public class AuthorConverter {

    private static AuthorConverter instance;
    public static AuthorConverter instance( ) {if(instance==null){instance=new AuthorConverter( );}return instance;}
    private AuthorConverter(){}

    public AuthorDto toCore(final RequestAuthor author) {
        AuthorDto authorDto = new AuthorDto( );
        authorDto.setName( author.getName( ) );
        authorDto.setType(author.getType().name());
        return authorDto;
    }
}
