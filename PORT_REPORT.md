=== Deep Analysis: tmp/constant_time_eq/src (rust) -> src/commonMain/kotlin/io/github/kotlinmania/constanttimeeq (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/constant_time_eq/src (rust)
  Files: 1
  Total imports: 2

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin/io/github/kotlinmania/constanttimeeq (kotlin)
  Files: 5
  Total imports: 12

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/constant_time_eq/src (1 files)
Target: src/commonMain/kotlin/io/github/kotlinmania/constanttimeeq (5 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   0 files
Unmatched: 1 source, 5 target


=== Missing from Target (need to port) ===

File                          Deps    Path
------------------------------------------------------------------------------
lib                           0       lib.rs

=== Porting Quality Summary ===

Matched by exact header:          0 / 0
Matched by provenance fallback:   0 / 0
Matched by name:                  0 / 0
Total TODOs in target: 0
Total lint errors:    0
Stub files:           0

=== Big Picture ===

- Missing files: 1
- Incomplete ports (similarity < 60%): 0
- Stub files: 0
- Files missing functions: 0 (total deficit: 0 functions)
- Type definitions missing: 0
- Files missing tests: 0 (total deficit: 0 unported `#[test]` functions)
- Documentation coverage: N/A (source has no docs)

Primary focus: create missing files (highest deps first)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 0
Missing files: 1


=== Missing Files (by Dependents) ===

Source File                   Expected Target                       Dependents Path
-----------------------------------------------------------------------------------------------------------------------

=== Reexport / Wiring Modules (consult, don't transliterate) ===

lib                           Lib                                   0          lib.rs

=== Documentation Gaps ===

Documentation coverage: N/A (source has no docs)
Files with >20% doc gap: 0

No significant documentation gaps found.

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
