package cat.controller;

import cat.entity.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import cat.bindingModel.CatBindingModel;
import cat.repository.CatRepository;

import java.util.List;

@Controller
public class CatController {
    private final CatRepository catRepository;

    @Autowired
    public CatController(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Cat> cats = catRepository.findAll();
        model.addAttribute("view", "cat/index");
        model.addAttribute("cats", cats);
        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("view", "cat/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(Model model, CatBindingModel catBindingModel) {
        Cat catObj = new Cat();

        if (catBindingModel == null) {
            return "redirect:/";
        }
        if (catBindingModel.getName().equals("") || catBindingModel.getNickname().equals("")
                || catBindingModel.getPrice() == 0) {
            return "redirect:/";
        }
        catObj.setName(catBindingModel.getName());
        catObj.setNickname(catBindingModel.getNickname());
        catObj.setPrice(catBindingModel.getPrice());

        this.catRepository.saveAndFlush(catObj);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable int id) {
        Cat catEntity = this.catRepository.findOne(id);

        if (catEntity == null) {
            return "redirect:/";
        }

        model.addAttribute("view", "cat/edit");
        model.addAttribute("cat", catEntity);
        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable int id, Model model, CatBindingModel catBindingModel) {
        Cat catEntity = this.catRepository.findOne(id);

        if (catEntity == null) {
            return "redirect:/";
        }

        catEntity.setName(catBindingModel.getName());
        catEntity.setNickname(catBindingModel.getNickname());
        catEntity.setPrice(catBindingModel.getPrice());

        this.catRepository.saveAndFlush(catEntity);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        Cat catEntity = this.catRepository.findOne(id);

        if (catEntity == null) {
            return "redirect:/";
        }
        model.addAttribute("view", "cat/delete");
        model.addAttribute("cat", catEntity);
        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable int id, CatBindingModel catBindingModel) {
        Cat catEntity = this.catRepository.findOne(id);
        if (catEntity == null) {
            return "redirect:/";
        }

        this.catRepository.delete(catEntity);

        return "redirect:/";
    }
}
