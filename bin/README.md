How to install common library
--------------------
```
mvn install -f pom.xml
```

Packages and classes design
----------------------------------

<table>
  <tr>
    <th>Packages</th>
    <th>Description</th>
    <th>Main classes</th>
  </tr>
  <tr>
    <td rowspan=2>io.pivotal.ambari_automation.infra</td>
    <td rowspan=2>Classes for infrastructure. Provisioning envionment, revert etc. This should be not depend on a particular infrastructure but should be abstracted (GPCloud vs EC2) </td>
    <td>InfraManager - interface for infra access</td>
  </tr>
  <tr>
    <td>GPCloudInfraManager - Implementaion for GPCloud</td>
  </tr>
  <tr>
    <td rowspan=2>io.pivotal.ambari_automation.ambari</td>
    <td rowspan=2>Clases for ambari. Installing ambari and its operations like deploying a cluster etc. </td>
    <td>AmbariInstaller - Install and uninstall Ambari-server </td>
  </tr>
  <tr>
    <td>AmbariAPIManager - Ambari Rest API wrapper </td>
  </tr>
  <tr>
    <td>io.pivotal.ambari_automation.util</td>
    <td>Clases for utilities. SSH etc. </td>
    <td>SSHUtil - ssh utility</td>
  </tr>
  <tr>
    <td>io.pivotal.ambari_automation.testng</td>
    <td>Clases for TestNG helpers. Custom listerners etc. </td>
    <td>AmbariTestListener - cusomter testng listener. </td>
  </tr>
</table>


