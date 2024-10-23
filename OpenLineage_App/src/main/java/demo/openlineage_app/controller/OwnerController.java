package demo.openlineage_app.controller;

import demo.openlineage_app.entity.Owner;
import demo.openlineage_app.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/owners")
    public String owners() {
        return "owners/create";
    }

    // Create a new owner object with the given details
    @PostMapping("/owners")
    public String createOwner(@ModelAttribute Owner owner) {

        ownerService.createOwner(owner);
        return "owners/success";
    }

    @GetMapping("/owners/query")
    public String query() {
        return "owners/query";
    }

    // Find the owner of a given id
    @PostMapping("/owners/query")
    public String queryOwners(@RequestParam("id") int id, Model model) {
        Owner owner = ownerService.findOwnerById(id);
        model.addAttribute("owner", owner);
        return "owners/queryResult";
    }

}
