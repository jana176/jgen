package ${packageName};

public enum ${enum.enumType} {

<#list enum.values as value> ${value}<#if value!=enum.values?last>,</#if></#list>

}