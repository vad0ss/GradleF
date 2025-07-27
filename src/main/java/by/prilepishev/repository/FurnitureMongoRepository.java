package by.prilepishev.repository;

import by.prilepishev.model.Furniture;
import by.prilepishev.model.Type;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureMongoRepository implements FurnitureRepository {

    private final MongoCollection<Document> collection;

    @Inject
    public FurnitureMongoRepository(@Named("MongoCollection") MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public void add(Furniture furniture) {
        Document doc = new Document("type", furniture.getType().name())
                .append("material", furniture.getMaterial())
                .append("price", furniture.getPrice())
                .append("color", furniture.getColor());
        collection.insertOne(doc);
    }

    @Override
    public void saveAll(List<Furniture> furnitureList) throws SQLException {
        List<Document> documents = new ArrayList<>();
        for (Furniture furniture : furnitureList) {
            documents.add(furnitureToDocument(furniture));
        }
        collection.insertMany(documents);
    }

    @Override
    public List<Furniture> findAll() throws SQLException {
        List<Furniture> furnitures = new ArrayList<>();

        for (Document document : collection.find()) {
            furnitures.add(documentToFurniture(document));
        }
        return furnitures;
    }

    @Override
    public List<Furniture> getByType(Type type) throws SQLException {
        return List.of();
    }

    private Document furnitureToDocument(Furniture furniture) {
        return new Document()
                .append("id", furniture.getId())
                .append("type", furniture.getType().getDisplayName())
                .append("material", furniture.getMaterial())
                .append("price", furniture.getPrice())
                .append("color", furniture.getColor());
    }

    private Furniture documentToFurniture(Document doc) {
        return new Furniture(
                doc.getInteger("id"),
                Type.fromDisplayName(doc.getString("type")),
                doc.getString("material"),
                doc.getInteger("price"),
                doc.getString("color")
        );
    }
}