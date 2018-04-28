create view dpview1 as
select a.*, b.wsdladdr
from dtprtlist1 a, servicelist b
where a.serviceid = b.serviceid;
                                                   

