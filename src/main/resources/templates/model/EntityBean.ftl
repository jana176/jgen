package ${class.typePackage};

import javax.persistence.*;
<#list class.importedPackages as package>
import ${package};
</#list>

@Entity
@Table(name="${class.name?lower_case}")
public class ${class.name} {  
<#list properties as property>
	<#if property.upper == 1 >   
	<#if property.id>
	@Id
	</#if>
	<#if property.classType??>
	@ManyToOne
	@JoinColumn(name="${property.type?lower_case}")
	</#if>
	<#if property.enumType??>
	@Enumerated(EnumType.ORDINAL)
	</#if>
    ${property.visibility} ${property.type} ${property.name};
    <#else> 
    @OneToMany(targetEntity=${property.type}.class, mappedBy="${class.name?lower_case}", fetch=FetchType.EAGER)
    ${property.visibility} Set<${property.type}> ${property.name} = new HashSet<${property.type}>();
    </#if>     
</#list>

	public ${class.name}(){}

<#list properties as property>
	<#if property.upper == 1 >   
	public ${property.type} get${property.name?cap_first}(){
    	return ${property.name};
    }
      
    public void set${property.name?cap_first}(${property.type} ${property.name}){
         this.${property.name} = ${property.name};
    }
      
    <#else>
	public Set<${property.type}> get${property.name?cap_first}(){
         return ${property.name};
    }
      
    public void set${property.name?cap_first}( Set<${property.type}> ${property.name}){
         this.${property.name} = ${property.name};
      }
    </#if>     
</#list>

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(this.${properties?first.name});
		return sb.toString();
	}

}
