__author__ = 'Hao Zou'

import calendar
import glob
import json
import logging
import os.path
import sys
import time
import logging

from adminnode import AdminNode
from request import ambari_request

logger = logging.getLogger(__name__)

def admin_node(machine_center):
    admin_node = machine_center.get_machine("adminnode")
    admin_node.__class__ = AdminNode
    admin_node.set_machinecenter(machine_center)
    admin_node.set_agents(machine_center.get_allmachines().remove_machine_by_host(admin_node.host))

    return admin_node

def get_ambari(admin_node, machine_center):
    if machine_center.get_property('sourcedir') is not None:
        sourcedir = machine_center.get_property('sourcedir')
        ambari = glob.glob("{0}/{1}".format(sourcedir, "ambari") + "*" + ".tar.gz")
        if not ambari:
            raise Exception("no ambari package found in %s", sourcedir)
        latest_ambari = os.path.basename(ambari[0])
        admin_node.send_file(ambari[0], '/usr/lib/%s' % latest_ambari)
    else:
        command = "wget %s -P /etc/yum.repos.d/" % machine_center.get_property("DistUrl").strip()
        machine_center.get_allmachines().run_command(command)
        latest_ambari = "ambari"
    return latest_ambari

def install_ambari(admin_node, machine_center):
    latest_ambari = get_ambari(admin_node, machine_center)
    if machine_center.get_property('sourcedir') is not None:
        ambari = "/usr/lib/{0}".format(latest_ambari)
        admin_node.run_command("tar -zxvf %s -C /usr/lib" % ambari)

        ambari_server =  ambari.rstrip(".tar.gz") + "/ambari-server*"

        ambari_agent = ambari.rstrip(".tar.gz") + "/ambari-agent*"
    else:
        ambari_server = latest_ambari+ "-server"
        ambari_agent = latest_ambari + "-agent"
    admin_node.install_ambari_server(ambari_server)
    admin_node.install_ambari_agent(ambari_agent)

