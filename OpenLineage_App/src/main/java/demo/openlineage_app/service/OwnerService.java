package demo.openlineage_app.service;

import demo.openlineage_app.entity.Owner;
import demo.openlineage_app.repository.OwnerRepository;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OwnerService
{
    @Autowired
    private OwnerRepository ownerRepository;
    private final OpenLineageService olservice;


    public OwnerService(@Qualifier("openLineageService_owner") OpenLineageService olservice)
    {
        this.olservice = olservice;
    }

    public void createOwner(Owner owner)
    {
        olservice.setJobName("create_owner");
        olservice.setQuery("INSERT IGNORE INTO owners (id, first_name, last_name, address, city, telephone)");
        olservice.setOutputNamespace("openlineage_test");
        olservice.setOutputName("openlineage.owners");

        OpenLineageClient client = olservice.openLineageClient();
        try{
            OpenLineage.RunEvent event = olservice.createEvent(OpenLineage.RunEvent.EventType.START, null, false, true);
            client.emit(event);

            ownerRepository.save(owner);

            event = olservice.createEvent(OpenLineage.RunEvent.EventType.COMPLETE, event.getRun().getRunId(), false, true);
            client.emit(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Owner findOwnerById(int id) {
        OpenLineageClient client = olservice.openLineageClient();
        olservice.setJobName("query_owner");
        olservice.setQuery("SELECT * FROM owners WHERE id = c");
        olservice.setInputNamespace("openlineage_test");
        olservice.setInputName("openlineage.owners");
        OpenLineage.RunEvent event = olservice.createEvent(OpenLineage.RunEvent.EventType.START, null, true, false);
        client.emit(event);
        Owner result = ownerRepository.findById(id).orElse(null);
        event = olservice.createEvent(OpenLineage.RunEvent.EventType.COMPLETE, event.getRun().getRunId(), true, false);
        client.emit(event);
        return result;
    }

}
