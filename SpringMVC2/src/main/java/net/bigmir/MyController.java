package net.bigmir;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MyController {

    private Map<Long, Photo> photos = new HashMap<Long, Photo>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty())
            throw new PhotoErrorException();

        try {
            long id = System.currentTimeMillis();
            Photo p = new Photo(id,"SomeName",photo.getBytes());
            photos.put(id, p);

            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException();
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    @RequestMapping(value = "/list")
    public String getList(Model model){
        model.addAttribute("list",new LinkedList<Photo>(photos.values()));
        return "list";
    }
    @RequestMapping(value = "/delChecked", method = RequestMethod.POST)
    public String delete(Model model, @RequestParam(required = false) long [] id){
        try {
            for (long ident: id) {
                photos.remove(ident);
            }
            return getList(model);
        } catch (NullPointerException e) {
            throw new PhotoNotFoundException();
        }
    }

    private ResponseEntity<byte[]> photoById(long id) {
        Photo photo = photos.get(id);
        if (photo.getBytes() == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<byte[]>(photo.getBytes(), headers, HttpStatus.OK);
    }
}
