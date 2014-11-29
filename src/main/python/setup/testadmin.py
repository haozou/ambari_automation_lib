'''
Created on Apr 18, 2013

@author: tony
'''
import logging

from laphone.laphonecase import LaphoneTestCase

import util


logger = logging.getLogger(__name__)

"""
All the tests for this class are defined in smoke_tests.SmokeTests.
"""
class TestAMBARI_deploy(LaphoneTestCase):

    @classmethod
    def setUpClass(cls):
        # validate that the configs provided are valid
        #util.validate_properties(cls.machine_center)

        #admin_node = util.admin_node(cls.machine_center)
        #util.try_set_clustername(admin_node, cls.machine_center)
        # install AMBARI, then deploy hadoop
        #util.install_ambari(admin_node, cls.machine_center)
        #util.deploy_cluster(admin_node, cls.machine_center)
        pass
    def test(self):
        pass
if __name__ == "__main__":
    import unittest
    unittest.main()
