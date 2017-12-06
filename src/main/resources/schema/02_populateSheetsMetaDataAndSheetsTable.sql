INSERT INTO sheetsmetadata VALUES(1, 'firstSheet', '[
   [{"type" : ["string", 6]}, { "type" : ["integer", 2]}, {"type" : ["set", ["one", "two", "three"]]}, { "type" : ["string", -1] }],
   ["name", "age", "likes", "description"]
]');

INSERT INTO sheets VALUES(1, '[
   ["Anand", 12, "one", "enjoys travel"],
   ["Dhaval", 10, "two", "enjoys food"],
   ["Shameer", 35, "three", "enjoys cars"],
   ["Senthil", 100, "four", "enjoys books"]
]
');