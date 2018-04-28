create view stprtview1 as
select a.*, b.wsdladdr
from stprtlist a, servicelist b
where a.serviceid = b.serviceid;
