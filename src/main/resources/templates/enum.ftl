package generated.model;

public enum ${name} {

<#list values as value> ${value}<#if value!=values?last>,</#if> </#list>

}