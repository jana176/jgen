package generated.repository;

<#list imports as package>
import ${package};
</#list>
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${repoClassName}Repository extends CrudRepository<${repoClassName}, ${idType}> {

}