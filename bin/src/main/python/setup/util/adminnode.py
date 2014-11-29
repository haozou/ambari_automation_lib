import os
import time
import glob
import json
import subprocess
import logging
from request import ambari_request
from laphone.machine import Machine

logger = logging.getLogger(__name__)

class AdminNode(Machine):
    def __init__(self, host, root_password):
        Machine.__init__(self, host, "root", root_password)
        self.clustername = "test"
        self.machine_center = None
        self.ambari_version = None
        self.agents = []
        self.blueprint = None
        self.hostmapping = None

    def set_clustername(self, clustername="test"):
        self.clustername = clustername

    def set_machinecenter(self, machinecenter):
        self.machine_center = machinecenter

    def set_agents(self, agents):
        self.agents = agents

    def set_blueprint(self, blueprint):
        self.blueprint = blueprint

    def set_hostmapping(self, hostmapping):
        self.hostmapping = hostmapping

    def get_latest_build(self, url, prefix, output_dir="./", rpm=True):
        """
        Get the latest build of package.

        :param url:  The URL of download web page
        :type url:  str.
        :param prefix:  The prefix of build (i.e. PHD-1.0)
        :type prefix:  str.
        :param output_dir:  The directory(is current directory by default) to store the downloaded packages
        :type output_dir:  str.
        :param rpm:  Representing if the build user wants to download is rpm
        :type rpm:  bool.
        :returns:  str. -- The downloaded package name

        """
        info = phdlib.util.get_latest_build(url, prefix, rpm)
        if info:
            self.run_command('wget -c -q "%s" -O "%s"' % (info[1], os.path.join(output_dir, info[0])))
            return info[0]
        else:
            raise Exception('Can not find a matched tar file at %s/%s*. Please check <tarname> and <link> in your machines.xml' % (url, prefix))

    def install_ambari_server(self, ambari_server, machine_center=None):
        print ambari_server
        self.run_command("yum -y install %s" % ambari_server)
        interact = self.interact_command("ambari-server setup", "root")
        interact.auto_input({'Customize user account for ambari-server daemon [y/n] (n)? ':'n\n' ,
                             'Enter choice (1):':'1\n',
                             'Do you accept the Oracle Binary Code License Agreement [y/n] (y)? ':'y\n',
                             'Enter advanced database configuration [y/n] (n)?':'n\n'}, 1800)
        interact.get_result(900)
        self.run_command("ambari-server start")

    def install_ambari_agent(self, ambari_agent):
        print ambari_agent
        if ambari_agent == "ambari-agent":
            #for agent in self.agents:
            #self.agents.run_command("wget http://public-repo-1.hortonworks.com/ambari/centos6/1.x/updates/1.6.1/ambari.repo -P /etc/yum.repos.d/")
            self.agents.run_command("yum -y install %s" % ambari_agent)
            self.agents.run_command("sed -i -e 's/hostname=localhost/hostname=%s/g' /etc/ambari-agent/conf/ambari-agent.ini" \
                              % self.machine_name_fqdn)
            self.agents.run_command("ambari-agent start")
        else:
            for agent in self.agents:
                self.run_command("scp %s root@%s:/usr/lib/ambari-agent.rpm" % (ambari_agent, agent.machine_name))
                agent.run_command("yum -y install /usr/lib/ambari-agent.rpm")
                agent.run_command("sed -i -e 's/hostname=localhost/hostname=%s/g' /etc/ambari-agent/conf/ambari-agent.ini" \
                              % self.machine_name_fqdn)
                agent.run_command("ambari-agent start")

    def deploy_cluster(self):
        '''
            deploy the ambari cluster and add the services based on the configure file
        '''
        command_head = "curl --user admin:admin -i -H \'X-Requested-By: Pivotal\' -X "
        if self.blueprint is not None:
            path = "/api/v1/blueprints/blueprint-c1"
            ret = ambari_request("POST", self.host, path, body=self.blueprint)
            logger.info(ret)
            time.sleep(20)
            path = "/api/v1/clusters/%s" % self.clustername
            ret = ambari_request("POST", self.host, path, body=self.hostmapping)
            logger.info(ret)

            doc = json.loads(ret)
            return doc['Requests']['id']
