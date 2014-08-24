package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public final class ScriptsIO {

    public static final char DEFAULT_ESCAPE_CHAR = '"';

    public static Collection<Record> loadCsv(String filename) throws IOException {
        try (InputStream stream = Files.newInputStream(Paths.get(filename), StandardOpenOption.READ)) {
            CsvSchema schema = CsvSchema.emptySchema().withHeader().withEscapeChar(DEFAULT_ESCAPE_CHAR);
            CsvMapper mapper = new CsvMapper();
            ObjectReader reader = mapper.reader(Map.class).with(schema);

            Iterator<Map<String, String>> rows = reader.readValues(stream);
            List<Record> records = new ArrayList<Record>();
            while (rows.hasNext()) {
                Map<String, String> row = rows.next();
                records.add(new Record(row));
            }
            return records;
        }
    }

    public static void saveCsv(String filename, Collection<Record> records) throws IOException {
        try (OutputStream stream = Files.newOutputStream(Paths.get(filename), StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Set<String> columns = new LinkedHashSet<String>();
            List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
            for (Record record : records) {
                Map<String, String> row = record.content();
                columns.addAll(row.keySet());
                rows.add(row);
            }

            CsvSchema.Builder schema = CsvSchema.builder().setUseHeader(true).setEscapeChar(DEFAULT_ESCAPE_CHAR);
            for (String column : columns) {
                schema.addColumn(column);
            }
            CsvMapper mapper = new CsvMapper();
            ObjectWriter writer = mapper.writer().withSchema(schema.build());
            writer.writeValue(stream, rows);
        }
    }

    public static void processCsv(String filename, Mediator... mediators) throws IOException {
        Collection<Record> records = loadCsv(filename);
        for (Record record : records) {
            for (Mediator mediator : mediators) {
                mediator.mediate(record);
            }
        }
    }

    private ScriptsIO() {
    }

}
