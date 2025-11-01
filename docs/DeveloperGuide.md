---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# PlayBook Developer Guide

#### Table of Contents

- [PlayBook Developer Guide](#playbook-developer-guide) - [Table of Contents](#table-of-contents)
  - [**Acknowledgements**](#acknowledgements)
  - [**Setting up, getting started**](#setting-up-getting-started)
  - [**Design**](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
  - [**Implementation**](#implementation)
    - [Proposed undo or redo feature](#proposed-undo-or-redo-feature)
      - [Proposed Implementation](#proposed-implementation)
      - [Design considerations:](#design-considerations)
  - [**Documentation, logging, testing, configuration, dev-ops**](#documentation-logging-testing-configuration-dev-ops)
  - [**Appendix: Requirements**](#appendix-requirements)
    - [Product scope](#product-scope)
    - [User stories](#user-stories)
    - [Use cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
  - [**Appendix: Instructions for manual testing**](#appendix-instructions-for-manual-testing)
    - [Launch and shutdown](#launch-and-shutdown)
    - [Deleting a player](#deleting-a-player)
    - [Assigning an injury status to a player](#assigning-an-injury-status-to-a-player)
    - [Saving data](#saving-data)
  - [**Appendix: Effort**](#appendix-effort)
    - [Difficulty Level](#difficulty-level)
    - [Challenges Faced](#challenges-faced)
    - [Effort Required](#effort-required)
    - [Achievements](#achievements)
    - [Reuse and Adaptation](#reuse-and-adaptation)

---

## **Acknowledgements**

PlayBook is adapted from AddressBook-Level3 by SE-EDU. We would like to acknowledge the following sources:

- [AB3 Tutorials & Guides](https://github.com/se-edu/guides)
- [AB3 Sample Code](https://github.com/nus-cs2103-AY2526S1/tp)

Libraries used:

- [JavaFX](https://openjfx.io/) - Used for building the graphical user interface
- [Jackson](https://github.com/FasterXML/jackson) - Used for JSON data serialization and deserialization
- [JUnit5](https://github.com/junit-team/junit5) - Used for unit testing

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

- At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

- [**`UI`**](#ui-component): The UI of the App.
- [**`Logic`**](#logic-component): The command executor.
- [**`Model`**](#model-component): Holds the data of the App in memory.
- [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues the command `delete pl/John Doe`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

- defines its _API_ in an `interface` with the same name as the Component.
- implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

- executes user commands using the `Logic` component.
- listens for changes to `Model` data so that the UI can be updated with the modified data.
- keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
- depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete pl/John Doe")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete pl/John Doe` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

- When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
- All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

- stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
- stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
- stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
- does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

- can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
- inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
- depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)
- `JsonAdaptedPerson` persists player details including primitive fields (`name`, `phone`, `email`, `address`, `isCaptain`) and JSON-adapted components — `JsonAdaptedInjury`, `JsonAdaptedTag`, `JsonAdaptedPosition`, and `JsonAdaptedTeam`.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Proposed undo or redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

- `VersionedAddressBook#commit()` — Saves the current address book state in its history.
- `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
- `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

- **Alternative 1 (current choice):** Saves the entire address book.

  - Pros: Easy to implement.
  - Cons: May have performance issues in terms of memory usage.

- **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  - Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  - Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

**Target user profile**:

- has a need to manage a significant number of contacts
- prefer desktop apps over other types
- can type fast
- prefers typing to mouse interactions
- is reasonably comfortable using CLI apps
- **for this evolution: semi-professional youth football coaches managing multiple teams**

**Value proposition**:

- manage contacts faster than a typical mouse/GUI driven app
- **specifically for football coaches: quickly organise and access player, parent, and assistant contacts across multiple teams**
- optimised for fast, command-line style data entry and squad management

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …   | I want to …                                    | So that I can…                                                       |
| -------- | -------- | ---------------------------------------------- | -------------------------------------------------------------------- |
| `* * *`  | new user | see usage instructions                         | refer to instructions when I forget how to use the App               |
| `* * *`  | coach    | add a new player                               | keep a record of their personal information                          |
| `* * *`  | coach    | add a new team                                 | organize players by teams                                            |
| `* * *`  | coach    | add a player to a team                         | maintain accurate team list for training and match selection         |
| `* * *`  | coach    | remove a player from a team                    | maintain accurate team list when player leaves team                  |
| `* * *`  | coach    | delete a player                                | clean up records of players I no longer coach                        |
| `* * *`  | coach    | delete a team                                  | clean up records of teams I no longer coach                          |
| `* * *`  | coach    | create a position                              | assign a position to a player                                        |
| `* * *`  | coach    | delete a position                              | delete a position that I created                                     |
| `* * *`  | coach    | assign a player a position                     | keep track of a player's position                                    |
| `* * *`  | coach    | create a named injury status with a timeframe  | standardize how injuries are tracked and managed                     |
| `* * *`  | coach    | assign an existing injury status to a player   | view and track their availability and rehab timeline                 |
| `* * *`  | coach    | list all the players                           | view all player information                                          |
| `* * *`  | coach    | search for a player by name                    | retrieve details of a specific player easily                         |
| `* * *`  | coach    | save a player's emails                         | have players email to send them documents                            |
| `* * *`  | coach    | save a player as captain                       | see who my team captains are                                         |
| `* * *`  | coach    | remove captain from a player                   | update leadership assignments when needed                            |
| `* *`    | coach    | filter players by captain status               | quickly view all captains                                            |
| `* *`    | coach    | filter players by team                         | focus only on players from a given team                              |
| `* *`    | coach    | filter player by injury                        | quickly check which players are unavailable                          |
| `* *`    | coach    | filter players by position                     | see all players who can play a certain role                          |
| `* *`    | coach    | remove an assigned injury status from a player | identify and select players who are fully fit                        |
| `* *`    | coach    | save players' past injury details              | identify higher-risk players and manage their workload appropriately |
| `*`      | coach    | create a shortlist of transfer targets         | consolidate potential signings for evaluation and outreach           |

### Use cases

(For all use cases below, the **System** is the `PlayBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add a player**

**MSS**

1.  User requests to add a player with all relevant details.
2.  PlayBook adds the player with the specified details.

    Use case ends.

**Extensions**

- 1a. The given player details are invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The player name already exists in the list.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 1c. The team specified for the player does not exist.

  - 1c1. PlayBook shows an error message prompting user to create the team first.

    Use case ends.

- 1d. A required field is missing.

  - 1d1. PlayBook shows an error message.

    Use case ends.

**Use case: UC02 - Delete a player**

**MSS**

1.  User requests to delete a specific player by name.
2.  PlayBook deletes the player and all associated data.

    Use case ends.

**Extensions**

- 1a. The given player name does not exist.

  - 1a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC03 - Add a team**

**MSS**

1.  User requests to add a team with a unique name.
2.  PlayBook adds the team with the specified name.

    Use case ends.

**Extensions**

- 1a. The given team name is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The team name already exists.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 1c. The team name is blank or empty.

  - 1c1. PlayBook shows an error message.

    Use case ends.

**Use case: UC04 - Delete a team**

**MSS**

1. User requests to delete a specific team by name.
2. PlayBook deletes the team.

   Use case ends.

**Extensions**

- 1a. The given team name does not exist.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 2a. The team has players assigned to it.

  - 2a1. PlayBook shows an error message indicating the team cannot be deleted while it has players.

    Use case ends.

**Use case: UC05 - Assign a player to a team**

**MSS**

1. User requests to reassign a specific player to a different team.
2. PlayBook reassigns the player to the new team.

   Use case ends.

**Extensions**

- 1a. The given player name does not exist.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The given team name does not exist.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 1c. The player is already in the specified team.

  - 1c1. PlayBook shows an error message indicating the player is already in that team.

    Use case ends.

- 1d. The player name or team name is invalid.

  - 1d1. PlayBook shows an error message.

    Use case ends.

- 2a. Player was a captain.

  - 2a1. PlayBook removes their captain status.
  - 2a2. PlayBook notifies the user.

    Use case ends.

**Use case: UC06 - Assign an injury status to a player**

**Guarantees**: Player's injury status is updated and persisted.

**MSS**

1.  User requests to list players.
2.  PlayBook shows a list of players.
3.  User requests to assign an existing injury status to a player by specifying the injury name and timeframe.
4.  PlayBook updates the player's availability and rehab timeline.

    Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The specified player does not exist in PlayBook.

  - 3a1. PlayBook shows an error message.

    Use case ends.

- 3b. The specified injury name is invalid (contains non-alphanumeric characters or is blank).

  - 3b1. PlayBook shows an error message.

    Use case ends.

- 3c. The specified injury is the default injury status, `FIT`.

  - 3c1. PlayBook shows an error message and prompts users to use the `unassigninjury` command instead.

    Use case ends.

- 3d. The specified injury status has already been assigned to the specified player.

  - 3d1. PlayBook shows an error message.

    Use case ends.

**Use case: UC07 - Remove an injury status from a player**

**Guarantees**: Player's injury status is removed and default `FIT` status is restored if player has no remaining injuries.

**MSS**

1.  User requests to list players.
2.  PlayBook shows a list of players.
3.  User requests to remove an injury status from an existing player by specifying the player name and injury name.
4.  PlayBook removes the specified injury from the player's injury list.

    Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The specified player does not exist in PlayBook.

  - 3a1. PlayBook shows an error message.

    Use case ends.

- 3b. The specified injury name is invalid (contains non-alphanumeric characters or is blank).

  - 3b1. PlayBook shows an error message.

    Use case ends.

- 3c. The specified player has no injuries (already has the default `FIT` status).

  - 3c1. PlayBook shows an error message.

    Use case ends.

- 3d. The specified injury status has not been assigned to the specified player before.

  - 3d1. PlayBook shows an error message.

    Use case ends.

**Use case: UC08 - List all players**

**MSS**

1.  User requests to list all players.
2.  PlayBook shows a list of players

    Use case ends.

**Extensions**

- 2a. The list is empty.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC09 - Search for a player**

**MSS**

1.  User requests to search for a player by name.
2.  PlayBook shows the player's details.

    Use case ends.

**Extensions**

- 1a. The player name is missing.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The player name is invalid.

  - 1b1. PlayBook shows an message.

    Use case ends.

- 2a. No player matches the given name.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC10 - Filter players by team**

**MSS**

1. User requests to filter players by a team name.

2. PlayBook shows the list of players in that team.

   Use case ends.

**Extensions**

- 1a. The team name is missing.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The team name is invalid.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 2a. The team does not exist.

  - 2a1. PlayBook shows an error message.

    Use case ends.

- 2b. The team exists but has no players.

  - 2b1. PlayBook shows an error message.

    Use case ends.

**Use case: UC11 - Filter players by injury**

**MSS**

1. User requests to filter players by an injury status.

2. PlayBook shows the list of players with that injury status.

**Extensions**

- 1a. The injury status is missing.

  - 1a1. PlayBook shows an message.

    Use case ends.

- 1b. The injury status is invalid.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 2a. No players match the given injury status.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC12 - Filter players by position**

**MSS**

1. User requests to filter players by a position.

2. PlayBook shows the list of players in that position.

   Use case ends.

**Extensions**

- 1a. The position is missing.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The position is invalid.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 2a. No players are in that position.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC13 - Create a position**

**MSS**

1.  User requests to create a position with specific details.

2.  PlayBook adds a position with given details.

    Use case ends.

**Extensions**

- 1a. Position detail is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 2a. Position already exists.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC14 - Delete a position**

**MSS**

1.  User requests to delete a position with specific details.

2.  PlayBook deletes a position with given details.

**Extensions**

- 1a. Position detail is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 2a. Position does not exist.

  - 2a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC15 - Assign position to a player**

**MSS**

1.  User requests to assign a position to a player with specific details.
2.  PlayBook assigns a position to a player with given details.

    Use case ends.

**Extensions**

- 1a. Position detail is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 2a. Player detail is invalid.

  - 2a1. PlayBook shows an error message.

    Use case ends.

- 3a. Position does not exist.

  - 3a1. PlayBook shows an error message.

    Use case ends.

- 4a. Player does not exist.

  - 4a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC16 - Save player's email**

**MSS**

1.  User requests to save a player's email under player's details.
2.  PlayBook updates email under player's detail.

    Use case ends.

**Extensions**

- 1a. The given player is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

**Use case: UC17 - Assign captain to a player**

**MSS**

1.  User requests to assign the captain role to a player.
2.  PlayBook updates the player's details to reflect they are now their team's captain.

    Use case ends.

**Extensions**

- 1a. The given player is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The player is already a captain.

  - 1b1. PlayBook shows an error message.

    Use case ends.

- 1c. Another player is already the captain.

  - 1c1. PlayBook unassigns previous captain.
  - 1c2. PlayBook assigns the player as new captain.

    Use case ends.

**Use case: UC18 - Remove captain from a player**

**MSS**

1.  User requests to remove the captain role from a player.
2.  PlayBook updates the player's details to reflect they are no longer captain.

    Use case ends.

**Extensions**

- 1a. The given player is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

- 1b. The player is not currently a captain.

  - 1b1. PlayBook shows an error message.

    Use case ends.

**Use case: UC19 - Filter players by captain status**

**MSS**

1.  User requests to filter players who are captains
2.  PlayBook shows the list of players marked as captains

    Use case ends.

**Extensions**

- 1a. No players are marked as captains.

  - 1a1. PlayBook shows an empty list message.

    Use case ends.

**Use case: UC19 - Save a player as captain**

**MSS**

1.  User requests to save a player as captain under player's details.
2.  PlayBook updates if player is captain under player's detail.

    Use case ends.

**Extensions**

- 1a. The given player is invalid.

  - 1a1. PlayBook shows an error message.

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 football players without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should be packaged into a single JAR file such that no additional installation steps are required.
5.  Should work fully offline and must not depend on any custom remote server for normal operations.
6.  User data should be stored locally in a human editable JSON file, without using a DBMS.
7.  GUI should render optimally at 1920x1080 and above (for 100% & 125% scaling) and remain fully functional at 1280x720 and above (for 150% scaling) on any _mainstream OS_.
8.  Response to any single user command should be visible within 3 seconds on any _mainstream OS_.
9.  User interface should be intuitive for football coaches with limited technical background to complete core tasks after reading the user guide once.
10. Sensitive player data should be safeguarded against accidental disclosure via manual export and delete options, and by running the app in a secure environment where local files are protected by the device's password.

### Glossary

- **AB3 (AddressBook-Level3)**: The original application that PlayBook was adapted from, developed by SE-EDU
- **API (Application Programming Interface)**: A set of definitions and protocols that specifies how software components should interact with each other
- **Captain**: A player designated as the leader of their team. Each team can have at most one captain
- **CLI (Command Line Interface)**: A text-based interface where users interact with the application by typing commands
- **Command**: A user instruction that performs a specific action in the application (e.g., `add`, `delete`, `filter`)
- **Entity**: A distinct object or concept in the system (e.g., Player, Team, Position, Injury)
- **FIT**: The default injury status indicating a player has no injuries and is available for selection
- **GUI (Graphical User Interface)**: The visual interface that displays information and allows user interaction through graphical elements
- **Injury**: A medical condition that affects a player's availability. A player can have multiple concurrent injuries
- **JAR (Java Archive)**: A package file format used to distribute Java applications as a single executable file
- **JSON (JavaScript Object Notation)**: A lightweight data format used to store and exchange data. PlayBook uses JSON files to persist data locally
- **Mainstream OS**: Windows, Linux, Unix, MacOS
- **Model**: The component that holds and manages the application's data in memory
- **Parser**: A component that interprets user input and converts it into executable commands
- **Player**: An individual footballer managed by the coach. Each player must belong to exactly one team
- **Position**: The role of a player on the field (e.g., Goalkeeper (GK), Center Back (CB), Striker (ST))
- **Team**: A named group of players (e.g., U15, First Team) managed by the coach
- **ViewType**: The current display mode of the UI (Players, Teams, or Positions panel)

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more _exploratory_ testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

### Deleting a player

1. Deleting a player while all players are being shown

   1. Prerequisites: List all players using the `list` command. Multiple players in the list.

   1. Test case: `delete pl/Bernice Yu`<br>
      Expected: Bernice Yu is deleted from the list. Details of the deleted contact shown in the status message.

   1. Test case: `delete pl/Invalid_Name`<br>
      Expected: No player is deleted. Error details shown in the status message.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` <br>
      Expected: Similar to previous.

### Assigning an injury status to a player

1. Assigning an injury status while all players are being shown

   1. Prerequisites: List all players using the `list` command. Multiple players in the list.

   1. Test case: `assigninjury pl/Alex Yeoh i/ACL`<br>
      Expected: Alex Yeoh's injury status is updated to include `ACL`.

   1. Test case: `assigninjury pl/Invalid_Name i/ACL`<br>
      Expected: No injury is assigned. Error details shown in the status message indicating invalid player name.

   1. Test case: `assigninjury pl/Alex Yeoh i/Invalid_Injury`<br>
      Expected: No injury is assigned. Error details shown in the status message indicating invalid injury name.

   1. Test case: `assigninjury pl/Alex Yeoh i/FIT`<br>
      Expected: No injury is assigned. Error details shown in the status message indicating `FIT` cannot be assigned as an injury status.

   1. Test case: `assigninjury pl/Alex Yeoh i/ACL` (after already assigning ACL)<br>
      Expected: No injury is assigned. Error details shown in the status message indicating injury status `ACL` has already been assigned to the player.

   1. Other incorrect delete commands to try: `assigninjury`, `assigninjury pl/Alex Yeoh`, `assigninjury i/ACL`, `...`<br>
      Expected: Similar to previous.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

---

## **Appendix: Effort**

### Difficulty Level

**High** - PlayBook represents a significant enhancement over AB3, managing multiple interconnected entity types with complex rules and validation logic.

### Challenges Faced

**Complex Entity Relationships**

- Managing 4 entity types (Person, Team, Position, Injury) with intricate relationships
- Implementing validation for team-player assignments with captain constraints
- Handling multiple concurrent injuries per player with proper state management

**Advanced Command System**

- Developing 10+ specialised commands beyond basic CRUD operations
- Implementing complex validation for injury assignments, captain assignments, and position assignments
- Creating multi-entity operations that span across different entity types

**Storage Complexity**

- Complex JSON serialisation with nested entities and collections
- Maintaining backward compatibility with legacy data formats
- Ensuring data integrity across multiple entity types during save/load operations

**UI Adaptation**

- Adapting the UI to display and manage multiple entity types (Players, Teams, Positions)
- Implementing filtering and display logic for different entity relationships

### Effort Required

The effort required for PlayBook was substantially higher than AB3 due to:

- **Multiple Entity Management**: While AB3 deals with only one entity type (Person), PlayBook manages four interconnected entity types
- **Complex Business Logic**: Implementing domain-specific rules for football team management
- **Advanced Validation**: Complex validation spanning across entity relationships
- **Enhanced User Experience**: More sophisticated command system and filtering capabilities

### Achievements

- Successfully implemented a comprehensive football team management system
- Maintained clean architecture while significantly expanding functionality
- Delivered a robust application with complex entity relationships
- Achieved feature parity with professional sports management tools

### Reuse and Adaptation

Approximately **10%** of development effort was saved through strategic reuse:

**Jackson Library** - Used for JSON data serialisation and deserialisation. Our work on adapting Jackson to handle PlayBook's complex entity relationships is contained in the storage component classes (`JsonAdaptedPerson`, `JsonAdaptedTeam`, `JsonAdaptedPosition`, `JsonAdaptedInjury`).

**JavaFX Framework** - Reused from AB3 for building the graphical user interface, but significantly adapted to support multiple entity displays and complex filtering operations.

**JUnit5 Testing Framework** - Reused for unit testing, with extensive test suites developed for the new entity types and complex business logic.
