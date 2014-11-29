import urllib2, base64

def ambari_request(method, host, path, port=8080, auth=('admin', 'admin'), headers={"X-Requested-By":"PIVOTAL"}, body=None):

    request = urllib2.Request("http://"+ host + ":%d" % port + path, body, headers)
    base64string = base64.encodestring('%s:%s' % auth).replace('\n', '')
    request.add_header("Authorization", "Basic %s" % base64string)
    request.get_methode = lambda:method

    try:
        connection = urllib2.urlopen(request)
    except urllib2.HTTPError, e:
        connection = e

    if connection.code in [200, 201, 202]:
            data = connection.read()
    else:
       raise Exception(connection.read())

    return data


if __name__ == '__main__':
    ret = ambari_request("GET", "ambari","/api/v1/clusters/test/requests/1?fields=tasks/Tasks/*")
    import json
    print ret
    doc = json.loads(ret)
    a = []
    for task in doc['tasks']:
        if task['Tasks']['status'] == 'COMPLETED':
            a.append(str(task['Tasks']['id']) + '\t' + task['Tasks']['role'] + '\t' + task['Tasks']['status'])
    print  '\n'.join(a)

    progress = 0
    import sys
    while 1:
        import time
        time.sleep(3)
        print '\r[{0}] {1}%'.format('#'*progress, progress),
        sys.stdout.flush()
        progress += 1
