/*
 * MysqlFactory.java
 *
 * Created on 21 de Julho de 2002, 3:07
 */

package pt.utl.ist.marc;

/**
 * @author Nuno Freire
 * @deprecated
 */
public interface MarcObjectFactory {

    public Record newRecord();

    public Field newField();

    public Subfield newSubfield();

    public boolean isFromThisFactory(Record rec);
}
