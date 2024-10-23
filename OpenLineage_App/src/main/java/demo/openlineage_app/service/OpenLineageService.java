package demo.openlineage_app.service;

import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.ConsoleTransport;
import io.openlineage.client.transports.HttpTransport;
import io.openlineage.client.utils.UUIDUtils;
import lombok.Setter;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Setter
public class OpenLineageService {

    private  String jobName;
    private  final String jobNamespace;
    private  String query;
    private  String inputNamespace;
    private  String inputName;
    private  String outputNamespace;
    private  String outputName;
    private final String role;
    private final Map<String, String> data;

    public OpenLineageService(String jobNamespace, String role, Map<String, String> data) {
        this.jobName = null;
        this.jobNamespace = jobNamespace;
        this.query = null;
        this.inputNamespace = null;
        this.inputName = null;
        this.outputNamespace = null;
        this.outputName = null;
        this.role = role;
        this.data = data;
    }
    public OpenLineageClient openLineageClient() {
        return   OpenLineageClient.builder()
                .transport(
                        HttpTransport.builder()
                                .uri("http://api:5002")
                                .build())
                .build();
    }

    public OpenLineage.RunEvent createEvent(OpenLineage.RunEvent.EventType eventType, UUID runId, Boolean input, Boolean output
    ) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        URI producer = URI.create(role);
        OpenLineage ol = new OpenLineage(producer);

        if (runId == null) {
            runId = UUIDUtils.generateNewUUID();
        }

        // run facets
        OpenLineage.RunFacets runFacets =
                ol.newRunFacetsBuilder()
                        .nominalTime(
                                ol.newNominalTimeRunFacetBuilder()
                                        .nominalStartTime(now)
                                        .nominalEndTime(now)
                                        .build())
                        .build();

        // a run is composed of run id, and run facets
        OpenLineage.Run run = ol.newRunBuilder().runId(runId).facets(runFacets).build();

        // job facets
        OpenLineage.JobFacets jobFacets =
                ol.newJobFacetsBuilder()
                        .sql(
                                ol.newSQLJobFacetBuilder()
                                        .query(query)
                                        .build()
                        )
                        .build();

        // job
        OpenLineage.Job job = ol.newJobBuilder().namespace(jobNamespace).name(jobName).facets(jobFacets).build();

        List<OpenLineage.InputDataset> inputs = null;
        // input dataset
        if(input) {
            inputs =
                    Arrays.asList(
                            ol.newInputDatasetBuilder()
                                    .namespace(inputNamespace)
                                    .name(inputName)
                                    .facets(
                                            ol.newDatasetFacetsBuilder()
                                                    .version(ol.newDatasetVersionDatasetFacet("1.1.1"))
                                                    .build())
                                    .inputFacets(
                                            ol.newInputDatasetInputFacetsBuilder()
                                                    .dataQualityMetrics(
                                                            ol.newDataQualityMetricsInputDatasetFacetBuilder()
                                                                    .rowCount(10L)
                                                                    .bytes(20L)
                                                                    .columnMetrics(
                                                                            ol.newDataQualityMetricsInputDatasetFacetColumnMetricsBuilder()
                                                                                    .put(
                                                                                            "mycol",
                                                                                            ol.newDataQualityMetricsInputDatasetFacetColumnMetricsAdditionalBuilder()
                                                                                                    .count(10D)
                                                                                                    .distinctCount(10L)
                                                                                                    .max(30D)
                                                                                                    .min(5D)
                                                                                                    .nullCount(1L)
                                                                                                    .sum(3000D)
                                                                                                    .quantiles(
                                                                                                            ol.newDataQualityMetricsInputDatasetFacetColumnMetricsAdditionalQuantilesBuilder()
                                                                                                                    .put("25", 52D)
                                                                                                                    .build())
                                                                                                    .build())
                                                                                    .build())
                                                                    .build())
                                                    .build())
                                    .build());
        }
        List<OpenLineage.SchemaDatasetFacetFields> fields = convertDataToFields(data, ol);
        // output dataset
        List<OpenLineage.OutputDataset> outputs = null;
        if(output){
            outputs =
                    Arrays.asList(
                            ol.newOutputDatasetBuilder()
                                    .namespace(outputNamespace)
                                    .name(outputName)
                                    .facets(
                                            ol.newDatasetFacetsBuilder()
                                                    .version(ol.newDatasetVersionDatasetFacet("output-version"))
                                                    .schema(
                                                            ol.newSchemaDatasetFacetBuilder()
                                                                    .fields(fields)
                                                                    .build())
                                                    .build())
                                    .outputFacets(
                                            ol.newOutputDatasetOutputFacetsBuilder()
                                                    .outputStatistics(ol.newOutputStatisticsOutputDatasetFacet(10L, 20L, 30L))
                                                    .build())
                                    .build());
        }
        // run state update which encapsulates all - with START event in this case
        OpenLineage.RunEvent runStateUpdate =
                ol.newRunEventBuilder()
                        .eventType(eventType)
                        .eventTime(now)
                        .run(run)
                        .job(job)
                        .inputs(inputs)
                        .outputs(outputs)
                        .build();

        return runStateUpdate;
    }

    public static List<OpenLineage.SchemaDatasetFacetFields> convertDataToFields(Map<String, String> data, OpenLineage ol) {
        List<OpenLineage.SchemaDatasetFacetFields> fields = new ArrayList<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            fields.add(
                    ol.newSchemaDatasetFacetFieldsBuilder()
                            .name(entry.getKey())
                            .type(entry.getValue())
                            .build()
            );
        }

        return fields;
    }

}