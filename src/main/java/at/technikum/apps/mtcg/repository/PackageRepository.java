package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.service.PackageService;
//import at.technikum.apps.mtcg.service.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PackageRepository {

    private final List<Package> packages = new ArrayList();

    public List<Package> findAll() {
        return this.packages;
    }

    public PackageRepository(){

    }

    public Optional<Package> find(int id) {
        return Optional.empty();
    }
    public Package save(Package cardPackage){
        //cardPackage.setId(this.packages.size() + 1);
        //this.packages.add(cardPackage);
        return cardPackage;
    }

    public Package delete(Package cardPackage) {return cardPackage;}
}
