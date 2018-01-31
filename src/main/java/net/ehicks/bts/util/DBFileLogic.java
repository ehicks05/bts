package net.ehicks.bts.util;

import net.ehicks.bts.UserSession;
import net.ehicks.bts.beans.Attachment;
import net.ehicks.bts.beans.DBFile;
import net.ehicks.eoi.EOI;

import java.util.List;

public class DBFileLogic
{
    public static void deleteDBFile(UserSession userSession, Long dbFileId)
    {
        DBFile dbFile = DBFile.getById(dbFileId);
        if (dbFile == null)
            return;

        // check if used by any attachments
        List<Attachment> attachments = Attachment.getByDbFileId(dbFile.getId());
        if (attachments.size() > 0)
            return;

        EOI.executeDelete(dbFile, userSession);

        DBFile thumbNail = dbFile.getThumbnail();
        if (thumbNail != null)
            EOI.executeDelete(thumbNail, userSession);
    }
}
