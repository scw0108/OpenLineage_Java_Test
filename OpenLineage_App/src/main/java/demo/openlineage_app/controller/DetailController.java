package demo.openlineage_app.controller;

import demo.openlineage_app.entity.Detail;
import demo.openlineage_app.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DetailController {

    @Autowired
    private DetailService detailService;

    @GetMapping("/details")
    public String details() {
        return "details/create";
    }
    @PostMapping("/details")
    public String createDetail(@RequestParam("id") int id, @RequestParam("interest") String interest) {
        detailService.createDetail(id, interest);
        return "details/success";
    }

    @GetMapping("/details/query")
    public String query() {
        return "details/query";
    }

    @PostMapping("/details/query")
    public String queryDetails(@RequestParam("id") int id, Model model) {
        Detail detail = detailService.findDetailsById(id);
        model.addAttribute("detail", detail);
        return "details/queryResult";
    }
}
