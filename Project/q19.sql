create view spview1 as
select a.*, b.wsdladdr
from stprtlist1 a, servicelist b
where a.serviceid = b.serviceid;
