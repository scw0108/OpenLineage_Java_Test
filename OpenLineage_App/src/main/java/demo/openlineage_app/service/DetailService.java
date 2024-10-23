package demo.openlineage_app.service;

import demo.openlineage_app.entity.Detail;
import demo.openlineage_app.entity.Owner;
import demo.openlineage_app.repository.DetailRepository;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DetailService {

    @Autowired
    private DetailRepository detailRepository;
    @Autowired
    private OwnerService ownerService;
    private final OpenLineageService olservice;


    public DetailService(@Qualifier("openLineageService_detail") OpenLineageService olservice)
    {
        this.olservice = olservice;
    }

    public void createDetail(int id, String interest) {
        Owner owner = ownerService.findOwnerById(id);
        Detail detail = new Detail();
        detail.setOwner(owner);
        detail.setFirstName(owner.getFirstName());
        detail.setLastName(owner.getLastName());
        detail.setInterest(interest);

        OpenLineageClient client = olservice.openLineageClient();
        olservice.setJobName("creat_detail");
        olservice.setQuery("INSERT INTO details (id, first_name, last_name, interest)");
        olservice.setInputNamespace("openlineage_test");
        olservice.setInputName("openlineage.owners");
        olservice.setOutputNamespace("openlineage_test");
        olservice.setOutputName("openlineage.details");

        OpenLineage.RunEvent event = olservice.createEvent(OpenLineage.RunEvent.EventType.START, null, true, true);
        client.emit(event);
        detailRepository.save(detail);
        event = olservice.createEvent(OpenLineage.RunEvent.EventType.COMPLETE, event.getRun().getRunId(), true, true);
        client.emit(event);
    }

    public Detail findDetailsById(int id) {
        OpenLineageClient client = olservice.openLineageClient();
        olservice.setJobName("query_detail");
        olservice.setQuery("SELECT * FROM details WHERE id = c");
        olservice.setInputNamespace("openlineage_test");
        olservice.setInputName("openlineage.details");
        OpenLineage.RunEvent event = olservice.createEvent(OpenLineage.RunEvent.EventType.START, null, true, false);
        client.emit(event);
        Detail result = detailRepository.findById(id).orElse(null);
        event = olservice.createEvent(OpenLineage.RunEvent.EventType.COMPLETE, event.getRun().getRunId(), true, false);
        client.emit(event);
        return result;
    }

}
