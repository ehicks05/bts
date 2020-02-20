package net.ehicks.bts.model;

import kotlin.Pair;
import net.ehicks.bts.TikaService;
import net.ehicks.bts.beans.AttachmentRepository;
import net.ehicks.bts.beans.AvatarRepository;
import net.ehicks.bts.beans.DBFile;
import net.ehicks.bts.beans.DBFileRepository;
import net.ehicks.bts.util.CommonIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class DBFileLogic
{
    private static final Logger log = LoggerFactory.getLogger(DBFileLogic.class);

    private DBFileRepository dbFileRepository;
    private AttachmentRepository attachmentRepository;
    private AvatarRepository avatarRepository;
    private TikaService tikaService;

    public DBFileLogic(DBFileRepository dbFileRepository, AttachmentRepository attachmentRepository, AvatarRepository avatarRepository, TikaService tikaService)
    {
        this.dbFileRepository = dbFileRepository;
        this.attachmentRepository = attachmentRepository;
        this.avatarRepository = avatarRepository;
        this.tikaService = tikaService;
    }

    // generate a thumbnail if the file is an image
    public Pair<DBFile, Exception> saveDBFile(MultipartFile file)
    {
        DBFile dbFile;
        try
        {
            byte[] fileBytes = file.getBytes();
            String mediaType = tikaService.detect(fileBytes, file.getOriginalFilename());

            DBFile thumbnail = null;
            if (mediaType.toLowerCase().startsWith("image")) // todo be more restrictive of image mediatypes?
            {
                byte[] scaledBytes = CommonIO.getThumbnail(file);
                thumbnail = new DBFile(0, scaledBytes, Arrays.hashCode(scaledBytes), mediaType, null);
                thumbnail = dbFileRepository.save(thumbnail);
            }

            dbFile = dbFileRepository.save(new DBFile(0, fileBytes, Arrays.hashCode(fileBytes), mediaType, thumbnail));
        }
        catch (IOException e)
        {
            log.error(e.getLocalizedMessage());
            return new Pair<>(null, e);
        }

        return new Pair<>(dbFile, null);
    }

    // only delete if the dbfile is no longer in use by any other objects
    public void deleteDBFile(Long dbFileId)
    {
        dbFileRepository.findById(dbFileId).ifPresent(dbFile -> {
            // check if used by any attachments
            if (!attachmentRepository.findByDbFile_id(dbFile.getId()).isEmpty())
                return;

            // check if used by any avatars
            if (!avatarRepository.findByDbFile_id(dbFile.getId()).isEmpty())
                return;

            dbFileRepository.delete(dbFile);

            DBFile thumbNail = dbFile.getThumbnail();
            if (thumbNail != null)
                dbFileRepository.delete(thumbNail);
        });
    }
}
