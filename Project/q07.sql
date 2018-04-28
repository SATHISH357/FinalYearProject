create view dtprtview1 as
select a.*, b.wsdladdr
from dtprtlist a, servicelist b
where a.serviceid = b.serviceid;
