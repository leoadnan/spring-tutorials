package repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import entity.SampleEntity;

@Repository
public interface SampleRepository extends CassandraRepository<SampleEntity> {


    @Query("SELECT count(*) FROM sample")
    public long testMethod();

    @Query("SELECT * FROM sample WHERE name = ?0")
    public List<SampleEntity> findByName(String testName);
    
    @Query("select count(*) from sample where name=?0 allow filtering")
    Long countByName(String name);
}