package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "xappes-mobilehub-1241857271-Usuarios")

public class UsuariosDO {
    private String _userId;
    private String _emailUsuario;
    private String _nameUsuario;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "emailUsuario")
    public String getEmailUsuario() {
        return _emailUsuario;
    }

    public void setEmailUsuario(final String _emailUsuario) {
        this._emailUsuario = _emailUsuario;
    }
    @DynamoDBAttribute(attributeName = "nameUsuario")
    public String getNameUsuario() {
        return _nameUsuario;
    }

    public void setNameUsuario(final String _nameUsuario) {
        this._nameUsuario = _nameUsuario;
    }

}
