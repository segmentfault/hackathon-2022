FROM quay.io/ceph/ceph:v16.2.6

RUN sed -i 's/enabled=1/enabled=0/g' /etc/yum.repos.d/Apache-Arrow.repo \
    && sed -i 's/enabled=1/enabled=0/g' /etc/yum.repos.d/tcmu-runner.repo \
    && sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-* \
    && sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-* \
    && yum -y install iscsi-initiator-utils

ARG ARCH=amd64

LABEL name="cinder-metal-csi" \
      license="Apache Version 2.0" \
      maintainers="Kubernetes Authors" \
      description="Cinder CSI Plugin" \
      architecture=$ARCH \
      distribution-scope="public" \
      summary="Cinder CSI Plugin" \
      help="none"

# Create a empty ceph.conf, in order to `rbd` can be run normally.
RUN mkdir /etc/ceph -p \
    && touch /etc/ceph/ceph.conf
COPY dist/cinder-metal-csi_linux_${ARCH}_v1/cinder-metal-csi /bin/
USER root
CMD ["/bin/cinder-metal-csi"]
