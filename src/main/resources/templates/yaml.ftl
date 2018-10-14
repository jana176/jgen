spring:
 datasource:
  driver-class-name: ${database.driverName}
  url: ${database.url}
  username: ${database.username}
  password: ${database.password}
  
 <#if database.overrideNamingConvention>
 jpa:
  hibernate:
   naming:
    physical-strategy: ${package}.RealNamingStrategyImpl
 </#if>