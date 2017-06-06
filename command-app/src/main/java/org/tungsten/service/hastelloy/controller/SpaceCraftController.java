package org.tungsten.service.hastelloy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tungsten.service.hastelloy.model.SpaceCraft;

@Controller
public class SpaceCraftController {

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(SpaceCraft spaceCraft) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
