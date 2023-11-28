package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.TransactionService;

public class PackageRepository {

    public Package save(Package package){
        PackageService.save(package);
    }
}
