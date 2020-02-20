package net.ehicks.bts.handlers;

import kotlin.Pair;
import net.ehicks.bts.beans.*;
import net.ehicks.bts.model.DBFileLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AvatarHandler
{
    private static final Logger log = LoggerFactory.getLogger(AvatarHandler.class);

    private AvatarRepository avatarRepository;
    private BtsSystemRepository btsSystemRepository;
    private UserRepository userRepository;
    private DBFileLogic dbFileLogic;

    public AvatarHandler(AvatarRepository avatarRepository, BtsSystemRepository btsSystemRepository,
                         UserRepository userRepository, DBFileLogic dbFileLogic)
    {
        this.avatarRepository = avatarRepository;
        this.btsSystemRepository = btsSystemRepository;
        this.userRepository = userRepository;
        this.dbFileLogic = dbFileLogic;
    }

    @GetMapping("/avatar/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getFromDatabase(@PathVariable Long id)
    {
        Avatar avatar = avatarRepository.findById(id)
                .orElse(btsSystemRepository.findFirstBy().getDefaultAvatar());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getDbFile().getMediaType()))
                .body(avatar.getDbFile().getContent());
    }

    @GetMapping("/avatar/default")
    @ResponseBody
    public ResponseEntity<byte[]> getDefault()
    {
        Avatar defaultAvatar = btsSystemRepository.findFirstBy().getDefaultAvatar();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(defaultAvatar.getDbFile().getMediaType()))
                .body(defaultAvatar.getDbFile().getContent());
    }

    @PostMapping("/admin/users/modify/updateAvatar")
    public ModelAndView updateAvatar(@RequestParam Long userId, @RequestParam Long fldAvatarId)
    {
        userRepository.findById(userId).ifPresent(user ->
                avatarRepository.findById(fldAvatarId).ifPresent(avatar -> {
                    user.setAvatar(avatar);
                    userRepository.save(user);
                }));

        return new ModelAndView("redirect:/admin/users/modify/form?userId=" + userId);
    }

    @PostMapping("/admin/users/modify/uploadAvatar")
    public ModelAndView uploadAvatar(@RequestParam Long userId, @RequestParam MultipartFile file)
    {
        Pair<DBFile, Exception> saveResult = dbFileLogic.saveDBFile(file);
        DBFile dbFile = saveResult.getFirst();
        Exception saveException = saveResult.getSecond();

        if (dbFile != null && saveException == null)
        {
            String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
            Avatar avatar = avatarRepository.save(new Avatar(0, filename, dbFile, false));

            userRepository.findById(userId).ifPresent(user -> {
                        user.setAvatar(avatar);
                        userRepository.save(user);
                    }
            );

            return new ModelAndView("redirect:/admin/users/modify/form?userId=" + userId)
                    .addObject("responseMessage", "Avatar added.");
        }

        return new ModelAndView("redirect:/admin/users/modify/form?userId=" + userId)
                .addObject("responseMessage", "There was an error uploading file.");
    }
}
