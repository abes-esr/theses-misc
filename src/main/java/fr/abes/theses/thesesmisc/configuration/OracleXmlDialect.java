package fr.abes.theses.thesesmisc.configuration;

import org.hibernate.dialect.Oracle10gDialect;

public class OracleXmlDialect extends Oracle10gDialect {
    @Override
    public boolean useInputStreamToInsertBlob() {
        //This forces the use of CLOB binding when inserting
        return false;
    }
}
