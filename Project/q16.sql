create view simview2 as
select a.wsdladdr, b.* from servicelist a, simview1 b
  where a.serviceid = b.serviceid;
