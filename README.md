# TownyPay

Created for my server TanukiCraft

play.tanukicraft.net

https://discord.gg/zd7pvJY9Zr

A plugin that pays Mayors and Kings on every new Towny day and create a daily budget that can be spent.

On my server I have disabled bank withdrawals to prevent rogue Mayors/Kings and to make them manage their finances. To allow Mayors/Kings to focus on growing their towns/nations TownyPay creates a daily pay for them.
To allow Mayors/Kings to pay other players, TownyPay introduces a pay command to pay from the town/nation bank within a daily budget.

This plugin has now been updated to provide some extra economy features

Resident Pay
A `/resident pay` command to send money to another player, replacing other economy pay commands to remove tax from the payee, take 10% tax from a fellow resident in the same town/nation. Otherwise, takes 20%

Resident Savings
Savings account for residents, allows them to transfer money into savings and receive interest each new town day.
When a resident sends money into their savings, it goes into holding. This cannot be accessed. On a new town day, any money in savings has interest applied, then any money in holdings gets transferred over.
This is to allow a buffer to prevent players abusing the system, players have to wait 2 new days before the interest is applied on money deposited.
Anything in savings can be deposited. Which means if they don't leave the money transferred from holdings to savings that day, they would not get the interest on it.

For Towny Admins:

Mayor and King pay can be configured to be either fixed or a percentage of the town profit.
Profit is calculated as CurrentBalance – (Upkeep x ConfiguredNumber)


Commands and Permissions:

You will need to add the permissions to all town and nation ranks you want access to these commands, at least Mayor, but you could add them to assistant, and even a Treasurer rank.


| Command                                              | Permission | Description                                                                                                            |
|------------------------------------------------------| -------------- |------------------------------------------------------------------------------------------------------------------------|
| `/town pay [resident] [amount]`                      | `townypay.command.town.pay` | Pays given resident the chosen amount from the town bank, if within budget.                                            |
| `/town finance`                                      | `townypay.command.town.finance` | Shows town budget information (Budget, Spend and Remaining)                                                            |
| `/town toggle [on/off]`                              | `townypay.command.town.togglepay` | Toggles Mayor pay on or off                                                                                            |
| `/town set pay [amount]`                             | `townypay.command.town.setpay` | Sets Mayors pay, must be between a configed amount                                                                     |
| `/nation pay [resident] [amount]`                    | `townypay.command.nation.pay` | Pays given resident the chosen amount from the town bank, if within budget.                                            |
| `/nation finance`                                    | `townypay.command.nation.finance` | Shows nation budget information (Budget, Spend and Remaining)                                                          |
| `/nation toggle [on/off]`                            | `townypay.command.town.togglepay` | Toggles King pay on or off                                                                                             |
| `/nation set pay [amount]`                           | `townypay.command.town.setpay` | Sets Kings pay, must be between a configed amount                                                                      |
| `/resident pay [resident] [amount]`                  | `townypay.command.resident.pay` | Pays given resident the chosen amount.                                                                                 |
| `/resident savings [info/deposit/withdraw] (amount)` | `townypay.command.resident.savings` | Info shows savings information, deposit deposits the amount into holdings, withdraw withdraws the amount from savings. |


